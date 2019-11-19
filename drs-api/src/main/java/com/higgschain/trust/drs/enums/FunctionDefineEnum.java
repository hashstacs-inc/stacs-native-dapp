package com.higgschain.trust.drs.enums;

import lombok.Getter;

/**
 * @author liuyu
 * @description
 * @date 2019-11-18
 */
@Getter
public enum FunctionDefineEnum {
     IDENTITY_SETTING("IDENTITY_SETTING","/identity/open/setting"),
     IDENTITY_BD_MANAGE("IDENTITY_BD_MANAGE","/identity/open/bd_manage"),
     BD_PUBLISH("BD_PUBLISH","/bd/open/publish"),
     PERMISSION_REGISTER("PERMISSION_REGISTER","/permission/open/register"),
     AUTHORIZE_PERMISSION("AUTHORIZE_PERMISSION","/permission/open/authorize"),
     CANCEL_PERMISSION("CANCEL_PERMISSION","/permission/open/cancel"),
     REGISTER_POLICY("REGISTER_POLICY","/manage/open/policy/register"),
     MODIFY_POLICY("MODIFY_POLICY","/manage/open/policy/modify"),
     SYSTEM_PROPERTY("SYSTEM_PROPERTY",""),
     KYC_SETTING("KYC_SETTING","/kyc/open/setting"),
     SET_FEE_RULE("SET_FEE_RULE",""),
     SAVE_ATTESTATION("SAVE_ATTESTATION",""),
     BUILD_SNAPSHOT("BUILD_SNAPSHOT",""),
     CREATE_CONTRACT("CREATE_CONTRACT","/contract/open/deploy"),
     CONTRACT_INVOKER("CONTRACT_INVOER","/contract/open/invoke")
     ;

    private String functionName;
    private String api;

    FunctionDefineEnum(String funcName,String api){
        this.functionName = funcName;
        this.api = api;
    }

    public static FunctionDefineEnum fromFuncName(String functionName){
        if(functionName == null){
            return null;
        }
        for(FunctionDefineEnum fd : values()){
            if(fd.getFunctionName().equals(functionName)){
                return fd;
            }
        }
        return null;
    }
}
