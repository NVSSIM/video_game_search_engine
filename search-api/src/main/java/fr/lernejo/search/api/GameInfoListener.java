package fr.lernejo.search.api;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

public class GameInfoListener {
    private final Logger logger = LoggerFactory.getLogger(GameInfoListener.class);
    private final RestHighLevelClient client;
    public  GameInfoListener(RestHighLevelClient client) {
        this.client = client;
    }
    @RabbitListener(queues = AmqpConfiguration.GAME_INFO_QUEUE)
    public void OnMessage(String mess, @Header("game_id") String gameId) throws IOException{
        IndexRequest request = new IndexRequest("games")
            .id(gameId)
            .source(mess, XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }
}
