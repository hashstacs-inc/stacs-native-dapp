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
package io.stacs.nav.drs.api.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Random;

import static java.util.Arrays.copyOfRange;

/**
 * The type Hash util.
 */
public class HashUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HashUtil.class);

    private static final Provider CRYPTO_PROVIDER;

    private static final String HASH_256_ALGORITHM_NAME;
    private static final String HASH_512_ALGORITHM_NAME;

    static {
        CRYPTO_PROVIDER = Security.getProvider("ECC");
        HASH_256_ALGORITHM_NAME = "ETH-KECCAK-256";
        HASH_512_ALGORITHM_NAME = "ETH-KECCAK-512";
    }

    /**
     * Sha 256 byte [ ].
     *
     * @param input - data for hashing
     * @return - sha256 hash of the data
     */
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest sha256digest = MessageDigest.getInstance("SHA-256");
            return sha256digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Sha 3 byte [ ].
     *
     * @param input the input
     * @return the byte [ ]
     */
    public static byte[] sha3(byte[] input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_256_ALGORITHM_NAME, CRYPTO_PROVIDER);
            digest.update(input);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Sha 3 byte [ ].
     *
     * @param input1 the input 1
     * @param input2 the input 2
     * @return the byte [ ]
     */
    public static byte[] sha3(byte[] input1, byte[] input2) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_256_ALGORITHM_NAME, CRYPTO_PROVIDER);
            digest.update(input1, 0, input1.length);
            digest.update(input2, 0, input2.length);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * hashing chunk of the data
     *
     * @param input  - data for hash
     * @param start  - start of hashing chunk
     * @param length - length of hashing chunk
     * @return - keccak hash of the chunk
     */
    public static byte[] sha3(byte[] input, int start, int length) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_256_ALGORITHM_NAME, CRYPTO_PROVIDER);
            digest.update(input, start, length);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Sha 512 byte [ ].
     *
     * @param input the input
     * @return the byte [ ]
     */
    public static byte[] sha512(byte[] input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_512_ALGORITHM_NAME, CRYPTO_PROVIDER);
            digest.update(input);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Ripemd 160 byte [ ].
     *
     * @param data - message to hash
     * @return - reipmd160 hash of the message
     */
    public static byte[] ripemd160(byte[] data) {
        Digest digest = new RIPEMD160Digest();
        if (data != null) {
            byte[] resBuf = new byte[digest.getDigestSize()];
            digest.update(data, 0, data.length);
            digest.doFinal(resBuf, 0);
            return resBuf;
        }
        throw new NullPointerException("Can't hash a NULL value");
    }

    /**
     * Calculates RIGTMOST160(SHA3(input)). This is used in address
     * calculations. *
     *
     * @param input - data
     * @return - 20 right bytes of the hash keccak of the data
     */
    public static byte[] sha3omit12(byte[] input) {
        byte[] hash = sha3(input);
        return copyOfRange(hash, 12, hash.length);
    }

    /**
     * Double digest byte [ ].
     *
     * @param input -
     * @return -
     * @see #doubleDigest(byte[], int, int) #doubleDigest(byte[], int, int)
     */
    public static byte[] doubleDigest(byte[] input) {
        return doubleDigest(input, 0, input.length);
    }

    /**
     * Calculates the SHA-256 hash of the given byte range, and then hashes the
     * resulting hash again. This is standard procedure in Bitcoin. The
     * resulting hash is in big endian form.
     *
     * @param input  -
     * @param offset -
     * @param length -
     * @return -
     */
    public static byte[] doubleDigest(byte[] input, int offset, int length) {
        try {
            MessageDigest sha256digest = MessageDigest.getInstance("SHA-256");
            sha256digest.reset();
            sha256digest.update(input, offset, length);
            byte[] first = sha256digest.digest();
            return sha256digest.digest(first);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't find such algorithm", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Random hash byte [ ].
     *
     * @return - generate random 32 byte hash
     */
    public static byte[] randomHash() {

        byte[] randomHash = new byte[32];
        Random random = new Random();
        random.nextBytes(randomHash);
        return randomHash;
    }

    /**
     * Short hash string.
     *
     * @param hash the hash
     * @return the string
     */
    public static String shortHash(byte[] hash) {
        return Hex.toHexString(hash).substring(0, 6);
    }
}
