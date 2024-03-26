package com.flab.readnshare.domain.auth.repository;

import com.flab.readnshare.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
