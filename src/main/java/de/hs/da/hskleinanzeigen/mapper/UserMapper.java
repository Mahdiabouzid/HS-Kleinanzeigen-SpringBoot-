package de.hs.da.hskleinanzeigen.mapper;

import de.hs.da.hskleinanzeigen.dto.user.UserAdRequestDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRegisterDTO;
import de.hs.da.hskleinanzeigen.dto.user.UserRequestDTO;
import de.hs.da.hskleinanzeigen.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    User toUser(UserAdRequestDTO dto);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "location", target = "location")
    User toUser(UserRegisterDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "location", target = "location")
    UserRequestDTO toUserResponse(User user);
}
