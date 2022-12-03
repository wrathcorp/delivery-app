package rso.project.delivery.models.converters;

import rso.project.delivery.lib.DeliveryMetadata;
import rso.project.delivery.models.entities.DeliveryMetadataEntity;

public class DeliveryMetadataConverter {

    public static DeliveryMetadata toDto(DeliveryMetadataEntity entity) {

        DeliveryMetadata dto = new DeliveryMetadata();
        dto.setDeliveryId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setRestaurantName(entity.getRestaurantName());
        dto.setLocation(entity.getLocation());
        dto.setDestinationLocation(entity.getDestinationLocation());
        dto.setRestaurantLocation(entity.getRestaurantLocation());

        return dto;

    }

    public static DeliveryMetadataEntity toEntity(DeliveryMetadata dto) {

        DeliveryMetadataEntity entity = new DeliveryMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setRestaurantName(dto.getRestaurantName());
        entity.setLocation(dto.getLocation());
        entity.setDestinationLocation(dto.getDestinationLocation());
        entity.setRestaurantLocation(dto.getRestaurantLocation());

        return entity;

    }

}
