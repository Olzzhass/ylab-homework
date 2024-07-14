package kaz.olzhas.ylab.mapper;

import kaz.olzhas.ylab.dao.BookingDao;
import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookingMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "workspaceId", source = "workspaceId")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    BookingDto toDto(Booking booking);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Booking toEntity(BookingDto bookingDto);
}
