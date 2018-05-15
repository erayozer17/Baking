package com.erayo.baking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Step {
    public int id;
    public String shortDescription;
    public String description;
    public String videoUrl;
    public String thumbnailUrl;
}
