package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoCategoryFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Category;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ICategoryPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CategoryUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    @Test
    void findByIdTest_success() {
        Long categoryId = 1L;
        Category category = new Category(categoryId, "Test Category", "Test Description");
        when(categoryPersistencePort.findById(categoryId)).thenReturn(category);

        Category result = categoryUseCase.findById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());
        verify(categoryPersistencePort, times(1)).findById(categoryId);
    }
    @Test
    void findByIdTest_NoCategoryFoundException() {
        Long id = 1L;
        when(categoryPersistencePort.findById(id)).thenReturn(null);

        assertThrows(
                NoCategoryFoundException.class,
                () -> {
                    categoryUseCase.findById(id);
                });
        verify(categoryPersistencePort, times(1)).findById(id);
    }


}