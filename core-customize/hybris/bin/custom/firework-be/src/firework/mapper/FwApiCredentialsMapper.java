package firework.mapper;

import firework.dto.FireworkCredentialsWsDTO;
import firework.model.FwApiCredentialsModel;


public interface FwApiCredentialsMapper {

    FireworkCredentialsWsDTO toWsDTO(FwApiCredentialsModel model);

    FwApiCredentialsModel toModel(FireworkCredentialsWsDTO fwApiCredentialsWsDTO);

    FwApiCredentialsModel mergeToModel(FwApiCredentialsModel targetToMerge, FireworkCredentialsWsDTO fwApiCredentialsWsDTO);
}
