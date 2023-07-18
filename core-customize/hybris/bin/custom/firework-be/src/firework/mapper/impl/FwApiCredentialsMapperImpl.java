package firework.mapper.impl;

import firework.dto.FireworkCredentialsWsDTO;
import firework.mapper.FwApiCredentialsMapper;
import firework.model.FwApiCredentialsModel;
import org.springframework.stereotype.Component;

@Component
public class FwApiCredentialsMapperImpl implements FwApiCredentialsMapper {

    @Override
    public FireworkCredentialsWsDTO toWsDTO(FwApiCredentialsModel model) {
        return FireworkCredentialsWsDTO.builder()
                .pk(model.getPk())
                .fireworkBusinessId(model.getFireworkBusinessId())
                .fireworkStoreId(model.getFireworkStoreId())
                .baseSiteId(model.getBaseSiteId())
                .hmacSigningKey(model.getHmacSigningKey())
                .build();
    }

    @Override
    public FwApiCredentialsModel toModel(FireworkCredentialsWsDTO fwApiCredentialsWsDTO) {
        FwApiCredentialsModel target = new FwApiCredentialsModel();

        target.setFireworkBusinessId(fwApiCredentialsWsDTO.getFireworkBusinessId());
        target.setFireworkStoreId(fwApiCredentialsWsDTO.getFireworkStoreId());
        target.setBaseSiteId(fwApiCredentialsWsDTO.getBaseSiteId());
        target.setHmacSigningKey(fwApiCredentialsWsDTO.getHmacSigningKey());
        target.setStoreUrl(fwApiCredentialsWsDTO.getStoreUrl());

        return target;
    }

    @Override
    public FwApiCredentialsModel mergeToModel(FwApiCredentialsModel targetToMerge, FireworkCredentialsWsDTO fwApiCredentialsWsDTO) {

        targetToMerge.setBaseSiteId(fwApiCredentialsWsDTO.getBaseSiteId());
        targetToMerge.setFireworkBusinessId(fwApiCredentialsWsDTO.getFireworkBusinessId());
        targetToMerge.setFireworkStoreId(fwApiCredentialsWsDTO.getFireworkStoreId());
        targetToMerge.setStoreUrl(fwApiCredentialsWsDTO.getStoreUrl());
        targetToMerge.setHmacSigningKey(fwApiCredentialsWsDTO.getHmacSigningKey());

        return targetToMerge;
    }
}
