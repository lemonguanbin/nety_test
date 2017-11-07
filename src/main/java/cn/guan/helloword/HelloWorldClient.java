package cn.guan.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by guanbinbin on 2017/11/7.
 */
public class HelloWorldClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "6666"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE,false)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new HelloWorldClientHandler());
                        }
                    });

            ChannelFuture future = b.connect(HOST, PORT).sync();

            future.channel().writeAndFlush("Hello Netty Server ,I am a common client");
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client2");
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client3");
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client4");
//            future.addListener(ChannelFutureListener.CLOSE);//离开了是会关闭链接，但channelread也就收不到东西了啊
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
