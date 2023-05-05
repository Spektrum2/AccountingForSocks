package sky.pro.accountingforsocks.component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sky.pro.accountingforsocks.dto.SocksRecord;
import sky.pro.accountingforsocks.model.Socks;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    @Mapping(target = "color", expression = "java(socksRecord.getColor().toLowerCase())")
    Socks toSocksEntity(SocksRecord socksRecord);
}
