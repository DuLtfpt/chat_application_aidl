// IMessageService.aidl
package com.example.sever;

// Declare any non-default types here with import statements
import com.example.sever.model.User;
import com.example.sever.model.Message;
import com.example.sever.model.ConversationItem;

interface IMessageService {

    List<Message> getMessageByConversation(in int conversationId);

    void deleteChat(in int conversationId);

    void deleteFriend(in int conversationId);

    void sendMesage(in String input,in int conversationId,in int receiveId,in int sendId);

    List<ConversationItem> getAllConversationByUserId(in int userId);

    User getUser(in int userId );
}