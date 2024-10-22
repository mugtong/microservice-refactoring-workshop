package app.anne.give.controller.transform;

import app.anne.give.controller.dto.GiveResponse;
import app.anne.give.domain.model.Give;
import app.anne.give.infrastructure.GiveRecord;

public class CreateGiveCommandDTOAssembler {
    public static GiveResponse toDTOFromRecord(GiveRecord record) {
        GiveResponse dto = new GiveResponse();
        dto.setGiveId(record.getId());
        dto.setOwnerId(record.getOwnerId());
        dto.setRequesterId(record.getRequesterId());
        dto.setPlaceId(record.getPlaceId());
        dto.setItemId(record.getItemId());
        dto.setItemIsbn(record.getItemIsbn());
        dto.setItemIsbn13(record.getItemIsbn13());
        dto.setItemTitle(record.getItemTitle());
        dto.setItemAuthor(record.getItemAuthor());
        dto.setItemPublisher(record.getItemPublisher());
        dto.setItemImageUrl(record.getItemImageUrl());
        dto.setStatus(record.getStatus());
        return dto;
    }

    public static GiveResponse toDTOFromEntity(Give entity) {
        GiveResponse dto = new GiveResponse();
        dto.setGiveId(entity.getId().getStringValue());
        dto.setOwnerId(entity.getOwnerId().getValue());
        dto.setRequesterId(entity.getRequester().getValue());
        dto.setPlaceId(entity.getPlace().getValue());
        dto.setItemId(entity.getItemId().getValue());
        dto.setItemIsbn(entity.getItem().getIsbn());
        dto.setItemIsbn13(entity.getItem().getIsbn13());
        dto.setItemTitle(entity.getItem().getTitle());
        dto.setItemAuthor(entity.getItem().getAuthor());
        dto.setItemPublisher(entity.getItem().getPublisher());
        dto.setItemImageUrl(entity.getItem().getImageUrl());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}
