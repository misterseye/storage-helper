package io.github.misterseye.validator;

import java.util.List;

public class ValidatorExtension {

    private static List<String> listExtensions= List.of(".jpeg",
            ".png",
            ".jpg",
            ".JPEG",
            ".pdf",
            ".docx",
            ".xlsx");

    public static boolean validateExtension(String name){
        String extension;
        extension = name.substring(name.lastIndexOf("."));
        return  listExtensions.contains(extension);
    }
}
