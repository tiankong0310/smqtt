package com.github.smqtt.core.http;

import com.github.smqtt.common.Receiver;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.json.JsonObjectDecoder;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

/**
 * @author luxurong
 * @date 2021/4/19 15:07
 * @description
 */
public class HttpReceiver implements Receiver {


    @Override
    public Mono<DisposableServer> bind() {
        return Mono.deferContextual(contextView -> {
            HttpConfiguration configuration = contextView.get(HttpConfiguration.class);
            return HttpServer
                    .create().port(configuration.getPort())
                    .route(new HttpRouterAcceptor())
                    .accessLog(configuration.getAccessLog())
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .wiretap(configuration.getWiretap())
                    .bind()
                    .cast(DisposableServer.class);
        });
    }
}