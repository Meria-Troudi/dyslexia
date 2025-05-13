package event.IA;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import java.io.IOException;

public class ChatbotAPI {

    // Remplace cette variable par ta propre clé API OpenAI
    private static final String API_KEY = "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"; // Remplace par ta clé API OpenAI

    // Méthode pour envoyer une question et obtenir la réponse de GPT-3
    public String getGPT3Response(String question) throws IOException {
        // Initialisation du client OkHttp
        OkHttpClient client = new OkHttpClient();

        // Préparation du corps de la requête avec la question et le modèle GPT-3
        String jsonBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]}";

        // Création de la requête HTTP avec les en-têtes nécessaires
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonBody
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")  // Mise à jour de l'URL
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)  // Ajout de la clé API dans l'en-tête
                .build();

        // Envoi de la requête et récupération de la réponse
        Response response = client.newCall(request).execute();

        // Vérification si la requête a réussi
        if (response.isSuccessful()) {
            return response.body().string();  // Retourne le corps de la réponse sous forme de chaîne de caractères
        } else {
            throw new IOException("Erreur dans la réponse : " + response.code() + " " + response.message());
        }
    }

    // Exemple d'utilisation de la méthode
    public static void main(String[] args) {
        ChatbotAPI chatbot = new ChatbotAPI();

        try {
            String question = "Quels sont les prochains événements ?";
            String response = chatbot.getGPT3Response(question);
            System.out.println("Réponse de GPT-3 : " + response);
        } catch (IOException e) {
            System.err.println("Erreur de communication avec l'API : " + e.getMessage());
        }
    }
}
