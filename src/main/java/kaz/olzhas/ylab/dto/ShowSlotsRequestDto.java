package kaz.olzhas.ylab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowSlotsRequestDto {
    private Long workspaceId;
    private String date;
}
