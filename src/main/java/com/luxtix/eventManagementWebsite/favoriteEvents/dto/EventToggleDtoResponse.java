package com.luxtix.eventManagementWebsite.favoriteEvents.dto;


import lombok.Data;

@Data
public class EventToggleDtoResponse {
    private Long id;
    private boolean isFavorite;
}
