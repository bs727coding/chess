package service;

import dataaccess.DataAccessException;
import request.LogoutRequest;
import result.LogoutResult;

public class LogoutService extends AuthService {
    LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        getAuthData(logoutRequest.authToken());
        deleteAuthData(logoutRequest.authToken());
        return new LogoutResult();
    }
}
