package ibm.elizapimentel.DummyProducts.mapper;

import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper( componentModel = "Spring" )
public interface ProductsMapper {
    ProductsMapper MAPPER = Mappers.getMapper(ProductsMapper.class);

    @Mapping(target = "id", ignore = true) // Ignorar a atualização do ID
    void updateModelFromDto(ProductsResponse dto, @MappingTarget ProductsRequest model);

    @Mapping(target = "id", ignore = true) // Ignorar a atualização do ID
    void updateDtoFromModel(ProductsRequest model, @MappingTarget ProductsResponse dto);

    ProductsResponse modelToDto(ProductsRequest model);

    @Mapping(target = "id", ignore = true)
    ProductsRequest dtoToModel(ProductsResponse dto);

}
