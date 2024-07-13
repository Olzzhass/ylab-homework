package kaz.olzhas.ylab.mapper;

import kaz.olzhas.ylab.dto.BookingDto;
import kaz.olzhas.ylab.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс {@code BookingMapper} отвечает за преобразование между сущностями бронирования и их DTO (Data Transfer Object).
 * Используется библиотекой MapStruct для автоматического создания реализации маппинга.
 */
@Mapper
public interface BookingMapper {

    /**
     * Преобразует сущность {@code Booking} в DTO {@code BookingDto}.
     *
     * @param booking сущность Booking, которую необходимо преобразовать.
     * @return соответствующий DTO BookingDto.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "workspaceId", source = "workspaceId")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    BookingDto toDto(Booking booking);

    /**
     * Преобразует DTO {@code BookingDto} в сущность {@code Booking}.
     *
     * @param bookingDto DTO BookingDto, которое необходимо преобразовать в сущность.
     * @return соответствующая сущность Booking.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Booking toEntity(BookingDto bookingDto);
}
