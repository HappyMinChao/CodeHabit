package com.bj.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	
	public static void main(String[] args) {
		//第一个线程组是用来用来接收client端连接的group
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//第二个线程组是实际处理业务请求的group
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		
		//在server端要创建serverBootstrap， 他是一个启动NIO服务的辅助启动类, 就是对server端进行一个配置
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup,workerGroup)  // 加入boss和worker， 把二者绑定起来
		.channel(NioServerSocketChannel.class)  //指定使用NIOServerSocketChannel这个通道， 如果使用http、 tcp、udp这的协议就不同了 相当于使用哪个通道
		//一定要使用childHandler去绑定具体的时间处理器, 写法固定
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				//
				sc.pipeline().addLast(new ServerHandler());
			}
		});
		
	}
		
}
