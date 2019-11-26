/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.stacs.nav.drs.api.solidity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.stacs.nav.drs.api.utils.ByteArrayWrapper;
import io.stacs.nav.drs.api.utils.ByteUtil;
import io.stacs.nav.drs.api.utils.HashUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;
import static org.apache.commons.collections4.ListUtils.select;
import static org.apache.commons.lang3.ArrayUtils.subarray;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.stripEnd;

public class Abi extends ArrayList<Abi.Entry> {
    private final static ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

    private static final String WORD_PATTERN = "(\\w*\\[\\]|\\w*)*";
    private static final String RESULT_PATTERN = "^\\((.*)\\)\\W";

    public static Abi fromJson(String json) {
        try {
            return DEFAULT_MAPPER.readValue(json, Abi.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String dumpResult(List<?> results) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Object obj : results) {
            sb.append("[" + index + "]=");
            if (obj instanceof byte[]) {
                sb.append(Hex.toHexString((byte[]) obj)).append("\n");
            } else if (obj instanceof Object[]) {
                sb.append("List\n");
                Object[] arr = (Object[]) obj;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] instanceof byte[]) {
                        sb.append(String.format("    [%s]=%s\n", i, Hex.toHexString((byte[]) arr[i])));
                    } else {
                        sb.append(String.format("    [%s]=%s\n", i, arr[i].toString()));
                    }
                }
            } else {
                sb.append(obj.toString()).append("\n");
            }
            index++;
        }
        return sb.toString();
    }

    private static List<String> extractWords(String text) {
        Pattern pattern = Pattern.compile(WORD_PATTERN);
        Matcher matcher = pattern.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            String word = matcher.group();
            if (StringUtils.isNoneBlank(word)) {
                words.add(word);
            }
        }
        return words;
    }

    private static byte[] encodeArguments(List<Entry.Param> inputs, Object... args) {
        if (args.length > inputs.size())
            throw new RuntimeException("Too many arguments: " + args.length + " > " + inputs.size());

        int staticSize = 0;
        int dynamicCnt = 0;
        // calculating static size and number of dynamic params
        for (int i = 0; i < args.length; i++) {
            SolidityType type = inputs.get(i).type;
            if (type.isDynamicType()) {
                dynamicCnt++;
            }
            staticSize += type.getFixedSize();
        }

        byte[][] bb = new byte[args.length + dynamicCnt][];
        for (int curDynamicPtr = staticSize, curDynamicCnt = 0, i = 0; i < args.length; i++) {
            SolidityType type = inputs.get(i).type;
            if (type.isDynamicType()) {
                byte[] dynBB = type.encode(args[i]);
                bb[i] = SolidityType.IntType.encodeInt(curDynamicPtr);
                bb[args.length + curDynamicCnt] = dynBB;
                curDynamicCnt++;
                curDynamicPtr += dynBB.length;
            } else {
                bb[i] = type.encode(args[i]);
            }
        }

        return ByteUtil.merge(bb);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Abi.Entry> T find(Class<T> resultClass, final Abi.Entry.Type type, final Predicate<T> searchPredicate) {
        return (T) CollectionUtils.find(this, entry -> entry.type == type && searchPredicate.evaluate((T) entry));
    }

    public Function findFunction(Predicate<Function> searchPredicate) {
        return find(Function.class, Abi.Entry.Type.function, searchPredicate);
    }

    public Event findEvent(Predicate<Event> searchPredicate) {
        return find(Event.class, Abi.Entry.Type.event, searchPredicate);
    }

    public Abi.Constructor findConstructor() {
        return find(Constructor.class, Entry.Type.constructor, object -> true);
    }

    @Override
    public String toString() {
        return toJson();
    }

    @JsonInclude(Include.NON_NULL)
    public static abstract class Entry {

        public final Boolean anonymous;
        public final Boolean constant;
        public final String name;
        public final List<Param> inputs;
        public final List<Param> outputs;
        public final Type type;
        public final Boolean payable;

        public Entry(Boolean anonymous, Boolean constant, String name, List<Param> inputs, List<Param> outputs, Type type, Boolean payable) {
            this.anonymous = anonymous;
            this.constant = constant;
            this.name = name;
            this.inputs = inputs;
            this.outputs = outputs;
            this.type = type;
            this.payable = payable;
        }

        @JsonCreator
        public static Entry create(@JsonProperty("anonymous") boolean anonymous,
                                   @JsonProperty("constant") boolean constant,
                                   @JsonProperty("name") String name,
                                   @JsonProperty("inputs") List<Param> inputs,
                                   @JsonProperty("outputs") List<Param> outputs,
                                   @JsonProperty("type") Type type,
                                   @JsonProperty(value = "payable", required = false, defaultValue = "false") Boolean payable) {
            Entry result = null;
            switch (type) {
                case constructor:
                    result = new Constructor(inputs, outputs);
                    break;
                case function:
                case fallback:
                    result = new Function(constant, name, inputs, outputs, payable);
                    break;
                case event:
                    result = new Event(anonymous, name, inputs, outputs);
                    break;
            }

            return result;
        }

        public String formatSignature() {
            StringBuilder paramsTypes = new StringBuilder();
            for (Entry.Param param : inputs) {
                paramsTypes.append(param.type.getCanonicalName()).append(",");
            }

            return  String.format("%s(%s)", name, stripEnd(paramsTypes.toString(), ","));
        }

        public byte[] fingerprintSignature() {
            return HashUtil.sha3(formatSignature().getBytes());
        }

        public byte[] encodeSignature() {
            return fingerprintSignature();
        }

        public enum Type {
            constructor,
            function,
            event,
            fallback
        }

        @JsonInclude(Include.NON_NULL)
        public static class Param {
            public Boolean indexed;
            public String name;
            public SolidityType type;

            public static List<?> decodeList(List<Param> params, byte[] encoded) {
                return decodeList(params, encoded, false);
            }

            public static List<?> decodeList(List<Param> params, byte[] encoded, boolean humanReadable) {
                List<Object> result = new ArrayList<>(params.size());

                int offset = 0;
                for (Param param : params) {
                    Object decoded = param.type.isDynamicType()
                            ? param.type.decode(encoded, SolidityType.IntType.decodeInt(encoded, offset).intValue())
                            : param.type.decode(encoded, offset);
                    if (humanReadable) {
                        if (param.type instanceof SolidityType.StringType) {
                            result.add(decoded);
                        } else if (param.type.isDynamicType()) {
                            List dynResult = new ArrayList<>();
                            Object[] arr = (Object[]) decoded;
                            for (Object value : arr) {
                                convertAndAdd(dynResult, value);
                            }
                            result.add(dynResult);
                        } else {
                            convertAndAdd(result, decoded);
                        }
                    } else {
                        result.add(decoded);
                    }

                    offset += param.type.getFixedSize();
                }

                return result;
            }

            private static void convertAndAdd(List<Object> result, Object value) {
                if ((value instanceof byte[])) {
                    result.add(new ByteArrayWrapper((byte[]) value));
                } else {
                    result.add(value);
                }
            }

            @Override
            public String toString() {
                return  String.format("%s%s%s", type.getCanonicalName(), (indexed != null && indexed) ? " indexed " : " ", name);
            }
        }
    }

    public static class Constructor extends Entry {

        public Constructor(List<Param> inputs, List<Param> outputs) {
            super(null, null, "", inputs, outputs, Type.constructor, false);
        }

        public static byte[] of(String constructor, byte[] code, Object... args) {
            List<Param> inputs = new ArrayList<>();
            List<String> names = extractWords(constructor);
            for (int i = 1; i < names.size(); i++) {
                Abi.Entry.Param param = new Abi.Entry.Param();
                param.type = SolidityType.getType(names.get(i));
                inputs.add(param);
            }

            byte[] argsData = Abi.encodeArguments(inputs, args);
            return ByteUtil.merge(code, argsData);
        }

        public List<?> decode(byte[] encoded) {
            return Param.decodeList(inputs, encoded);
        }

        public String formatSignature(String contractName) {
            return  String.format("function %s(%s)", contractName, join(inputs, ", "));
        }


    }

    public static class Function extends Entry {

        private static final int ENCODED_SIGN_LENGTH = 4;

        public Function(boolean constant, String name, List<Param> inputs, List<Param> outputs, Boolean payable) {
            super(null, constant, name, inputs, outputs, Type.function, payable);
        }

        public static Function of(String func) {
            Pattern pattern = Pattern.compile(RESULT_PATTERN);
            List<Param> inputs = new ArrayList<>();
            List<Param> outputs = new ArrayList<>();

            Matcher matcher = pattern.matcher(func);
            if (matcher.find()) {
                func = func.replace(matcher.group(), "");
                String results = matcher.group(1);
                if (StringUtils.isNotEmpty(results)) {
                    extractWords(results).forEach(result -> {
                        Abi.Entry.Param param = new Abi.Entry.Param();
                        param.type = SolidityType.getType(result);
                        outputs.add(param);
                    });
                }
            }

            List<String> names = extractWords(func);
            for (int i = 1; i < names.size(); i++) {
                Abi.Entry.Param param = new Abi.Entry.Param();
                param.type = SolidityType.getType(names.get(i));
                inputs.add(param);
            }

            Abi.Function function = new Abi.Function(false, names.get(0), inputs, outputs, false);
            return function;
        }

        public static byte[] extractSignature(byte[] data) {
            return subarray(data, 0, ENCODED_SIGN_LENGTH);
        }

        public byte[] encode(Object... args) {
            return ByteUtil.merge(encodeSignature(), encodeArguments(args));
        }

        public byte[] encodeArguments(Object... args) {
            return Abi.encodeArguments(inputs, args);
        }

        public List<?> decode(byte[] encoded) {
            return Param.decodeList(inputs, subarray(encoded, ENCODED_SIGN_LENGTH, encoded.length));
        }

        public List<?> decodeResult(byte[] encoded) {
            return Param.decodeList(outputs, encoded, false);
        }

        public List<?> decodeResult(byte[] encoded, boolean humanReadable) {
            return Param.decodeList(outputs, encoded, humanReadable);
        }

        @Override
        public byte[] encodeSignature() {
            return extractSignature(super.encodeSignature());
        }

        @Override
        public String toString() {
            String returnTail = "";
            if (constant) {
                returnTail += " constant";
            }
            if (!outputs.isEmpty()) {
                List<String> types = new ArrayList<>();
                for (Param output : outputs) {
                    types.add(output.type.getCanonicalName());
                }
                returnTail += String.format(" returns(%s)", join(types, ", "));
            }

            return String.format("function %s(%s)%s;", name, join(inputs, ", "), returnTail);
        }
    }

    public static class Event extends Entry {

        public Event(boolean anonymous, String name, List<Param> inputs, List<Param> outputs) {
            super(anonymous, null, name, inputs, outputs, Type.event, false);
        }

        public List<?> decode(byte[] data, byte[][] topics) {
            List<Object> result = new ArrayList<>(inputs.size());

            byte[][] argTopics = anonymous ? topics : subarray(topics, 1, topics.length);
            List<Param> indexedParams = filteredInputs(true);
            List<Object> indexed = new ArrayList<>();
            for (int i = 0; i < indexedParams.size(); i++) {
                Object decodedTopic;
                if (indexedParams.get(i).type.isDynamicType()) {
                    // If arrays (including string and bytes) are used as indexed arguments,
                    // the Keccak-256 hash of it is stored as topic instead.
                    decodedTopic = SolidityType.Bytes32Type.decodeBytes32(argTopics[i], 0);
                } else {
                    decodedTopic = indexedParams.get(i).type.decode(argTopics[i]);
                }
                indexed.add(decodedTopic);
            }
            List<?> notIndexed = Param.decodeList(filteredInputs(false), data);

            for (Param input : inputs) {
                result.add(input.indexed ? indexed.remove(0) : notIndexed.remove(0));
            }

            return result;
        }

        private List<Param> filteredInputs(final boolean indexed) {
            return select(inputs, param -> param.indexed == indexed);
        }

        @Override
        public String toString() {
            return  String.format("event %s(%s);", name, join(inputs, ", "));
        }
    }
}
