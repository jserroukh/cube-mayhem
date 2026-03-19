package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitaire pour gérer les fichiers JSON. Permet de lire, écrire et
 * manipuler des fichiers JSON.
 */
public class JSONFile {

    private String path;
    private final ObjectMapper objectMapper;
    private Map<String, Object> jsonObject;

    /**
     * Constructeur de la classe JSONFile.
     *
     * @param path Chemin du fichier JSON.
     */
    public JSONFile(String path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
        this.jsonObject = new HashMap<>();
    }

    /**
     * Crée un fichier JSON vide.
     */
    public void create() {
        this.jsonObject = new HashMap<>();
        Path filePath = Paths.get(this.path);

        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            this.saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Écrit une ligne dans le fichier JSON.
     *
     * @param key Clé à écrire.
     * @param value Valeur associée à la clé.
     */
    public void writeLine(String key, Object value) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = jsonObject;

        // Création automatique des clés intermédiaires si elles n'existent pas
        for (int i = 0; i < keys.length - 1; i++) {
            if (!current.containsKey(keys[i]) || !(current.get(keys[i]) instanceof Map)) {
                current.put(keys[i], new HashMap<>());
            }
            current = (Map<String, Object>) current.get(keys[i]);
        }

        // Mise à jour de la clé finale
        current.put(keys[keys.length - 1], value);
        this.saveToFile();
    }

    /**
     * Lit une valeur dans le fichier JSON.
     *
     * @param key Clé à lire.
     * @return Valeur associée à la clé.
     */
    public Object read(String key) {
        this.open();
        return readValueByKey(this.jsonObject, key);
    }

    private Object readValueByKey(Map<String, Object> root, String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = root;

        for (int i = 0; i < keys.length - 1; i++) {
            String keyPart = keys[i];

            if (keyPart.contains("[")) {
                String arrayName = keyPart.substring(0, keyPart.indexOf("["));
                int index = Integer.parseInt(keyPart.substring(keyPart.indexOf("[") + 1, keyPart.indexOf("]")));

                Object obj = current.get(arrayName);
                if (obj instanceof List) {
                    List<?> list = (List<?>) obj;
                    if (index < list.size()) {
                        current = (Map<String, Object>) list.get(index);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                Object obj = current.get(keyPart);
                if (obj instanceof Map) {
                    current = (Map<String, Object>) obj;
                } else {
                    return obj;
                }
            }
        }
        return current.get(keys[keys.length - 1]);
    }

    /**
     * Ouvre le fichier JSON et charge son contenu.
     */
    private void open() {
        File file = new File(this.path);
        if (!file.exists() || file.length() == 0) {
            this.jsonObject = new HashMap<>();
            System.err.println("Le fichier JSON est vide ou introuvable. Création d'un JSON vide.");
            return;
        }

        try {
            String content = Files.readString(Paths.get(this.path));
            this.jsonObject = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            this.jsonObject = new HashMap<>();
            System.err.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde les données dans le fichier JSON.
     */
    private void saveToFile() {
        try (FileWriter writer = new FileWriter(this.path)) {
            writer.write(toJsonString(this.jsonObject, 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit un objet en chaîne JSON formatée.
     *
     * @param obj Objet à convertir.
     * @param indentLevel Niveau d'indentation.
     * @return Chaîne JSON formatée.
     */
    private String toJsonString(Object obj, int indentLevel) {
        String indent = "\t".repeat(indentLevel);
        String indentInner = "\t".repeat(indentLevel + 1);

        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder("{\n");
            Map<String, Object> map = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(indentInner)
                        .append("\"").append(entry.getKey()).append("\": ")
                        .append(toJsonString(entry.getValue(), indentLevel + 1))
                        .append(",\n");
            }
            if (!map.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("\n").append(indent).append("}");
            return sb.toString();
        } else if (obj instanceof List) {
            StringBuilder sb = new StringBuilder("[\n");
            List<?> list = (List<?>) obj;
            for (Object item : list) {
                sb.append(indentInner)
                        .append(toJsonString(item, indentLevel + 1))
                        .append(",\n");
            }
            if (!list.isEmpty()) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("\n").append(indent).append("]");
            return sb.toString();
        } else if (obj instanceof String) {
            return "\"" + obj + "\"";
        } else {
            return obj.toString();
        }
    }

}
