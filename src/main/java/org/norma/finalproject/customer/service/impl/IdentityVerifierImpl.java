package org.norma.finalproject.customer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.customer.service.IdentityVerifier;
import org.springframework.stereotype.Service;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@Slf4j
public class IdentityVerifierImpl implements IdentityVerifier {
    /*
        This service is like a e-devlet identity verifier. only senario.
    */

    @Override
    public boolean verify(String identity) {
        log.info(identity + " Identity number successfully verified.");
        return true;
    }
}
