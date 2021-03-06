package com.devicehive.service;

import com.devicehive.configuration.Messages;
import com.devicehive.dao.IdentityProviderDao;
import com.devicehive.exceptions.IllegalParametersException;
import com.devicehive.util.HiveValidator;
import com.devicehive.vo.IdentityProviderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * Created by tmatvienko on 11/17/14.
 */
@Component
public class IdentityProviderService {

    @Autowired
    private HiveValidator hiveValidator;

    @Autowired
    private IdentityProviderDao identityProviderDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    public IdentityProviderVO find(@NotNull String name) {
        return identityProviderDao.getByName(name);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(@NotNull String name) {
        return identityProviderDao.deleteById(name);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public IdentityProviderVO update(@NotNull String providerName, IdentityProviderVO identityProvider) {
        if (!StringUtils.equals(providerName, identityProvider.getName())) {
            throw new IllegalParametersException(String.format(Messages.IDENTITY_PROVIDER_NAME_CHANGE_NOT_ALLOWED, providerName, identityProvider.getName()));
        }
        IdentityProviderVO existing = find(providerName);
        if (existing == null) {
            throw new IllegalParametersException(String.format(Messages.IDENTITY_PROVIDER_NOT_FOUND, providerName));
        }
        if (identityProvider.getName() != null) {
            existing.setName(identityProvider.getName());
        }
        if (identityProvider.getApiEndpoint() != null) {
            existing.setApiEndpoint(identityProvider.getApiEndpoint());
        }
        hiveValidator.validate(existing);
        return identityProviderDao.merge(existing);
    }
}
