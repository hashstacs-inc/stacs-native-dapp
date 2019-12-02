package io.stacs.nav.drs.api;

import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.attestation.SaveAttestationVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.contract.ContractCreateVO;
import io.stacs.nav.drs.api.model.contract.ContractInvokeVO;
import io.stacs.nav.drs.api.model.fee.FeeTxRuleConfigVO;
import io.stacs.nav.drs.api.model.identity.IdentityBDManageVO;
import io.stacs.nav.drs.api.model.identity.IdentitySettingVO;
import io.stacs.nav.drs.api.model.identity.KYCSettingVO;
import io.stacs.nav.drs.api.model.permission.AuthPermissionVO;
import io.stacs.nav.drs.api.model.permission.CancelPermissionVO;
import io.stacs.nav.drs.api.model.permission.RegisterPermissionVO;
import io.stacs.nav.drs.api.model.policy.ModifyPolicyVO;
import io.stacs.nav.drs.api.model.policy.RegisterPolicyVO;
import io.stacs.nav.drs.api.model.property.SystemPropertyConfigVO;
import io.stacs.nav.drs.api.model.snapshot.BuildSnapshotVO;

/**
 * @author suimi
 * @date 2019/10/30
 */
public interface ISubmitterService {

    /**
     * publish business define
     */
    void publishBD(BusinessDefine bd) throws DappException;

    /**
     * contract creation
     */
    void contractPublish(ContractCreateVO vo) throws DappException;

    /**
     * contract invoke
     */
    void contractInvoke(ContractInvokeVO vo) throws DappException;

    /**
     * register policy
     */
    void registerPolicy(RegisterPolicyVO vo) throws DappException;

    /**
     * modify policy
     */
    void modifyPolicy(ModifyPolicyVO vo) throws DappException;

    /**
     * register permission
     */
    void registerPermission(RegisterPermissionVO vo) throws DappException;

    /**
     * authorize permission
     */
    void authPermission(AuthPermissionVO vo) throws DappException;

    /**
     * cancel permission
     */
    void cancelPermission(CancelPermissionVO vo) throws DappException;

    /**
     * set identity
     */
    void identitySetting(IdentitySettingVO vo) throws DappException;

    /**
     * identity bd manage
     */
    void identityManager(IdentityBDManageVO vo) throws DappException;

    /**
     * setting KYC
     */
    void settingKYC(KYCSettingVO vo) throws DappException;

    void feeTxRuleConfig(FeeTxRuleConfigVO vo) throws DappException;

    void systemPropertyConfig(SystemPropertyConfigVO vo) throws DappException;

    void buildSnapshot(BuildSnapshotVO vo) throws DappException;

    void saveAttestation(SaveAttestationVO vo) throws DappException;
}
