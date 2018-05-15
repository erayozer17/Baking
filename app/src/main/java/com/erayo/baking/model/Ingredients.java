package com.erayo.baking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ingredients {
    public int quantity;
    public String measure;
    public String ingredient;
}
