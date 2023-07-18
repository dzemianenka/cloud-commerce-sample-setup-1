package firework.mapper;

import firework.dto.FireworkAuthorizationResponseDto;
import firework.dto.FireworkOAuthWsDTO;
import firework.dto.FireworkResponseDto;
import firework.model.FwOAuthAppModel;

public interface FwOAuthAppMapper {
    FireworkOAuthWsDTO toWsDTO(FwOAuthAppModel model);

    FwOAuthAppModel toModel(FireworkOAuthWsDTO fwOAuthApp);

    FwOAuthAppModel mergeToModel(FwOAuthAppModel targetToMerge, FireworkOAuthWsDTO fwOAuthApp);

    FireworkOAuthWsDTO mergeExternalOAuthToWsDTO(FireworkOAuthWsDTO target, FireworkResponseDto response);

    void mergeExternalCredentialsToWsDTO(FireworkOAuthWsDTO target, FireworkAuthorizationResponseDto response);
}
