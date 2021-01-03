package com.ly.storeserver.ext.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description:
 * @Author wangy
 * @Date 2020/12/29 20:44
 * @Version V1.0.0
 **/
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            //绑定监听
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置为非租塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //监听
    public void listen() {
        try{
            while (true) {
                int select = selector.select(10000);
                if (select > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        if (next.isAcceptable()) {
                            SocketChannel accept = listenChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);
                            System.out.println(accept.getRemoteAddress() + "上线");
                        }
                        if(next.isReadable()) {
                            readData(next);
                        }
                        //注意删除当前的key
                        iterator.remove();
                    }
                } else {
                    //System.out.println("等待...");
                }
            }
        } catch (Exception e) {

        }
    }

    //读取客户端消息
    private void readData(SelectionKey key) {
        //得到channel
        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int count = channel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("form客户端：" + msg);
                //向其他的客户端发送消息
                sendInfoToOtherClients(msg, channel);
            }
        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了..");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    //把消息发送给其他客户端
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector上的socketchannel，并排除自己
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            Channel channel = key.channel();
            //排除自己
            if (self instanceof SocketChannel && channel != self) {
                SocketChannel dest = (SocketChannel) channel;
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                dest.write(wrap);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
    }
}
