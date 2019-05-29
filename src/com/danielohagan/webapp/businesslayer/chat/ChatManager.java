package com.danielohagan.webapp.businesslayer.chat;

import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private List<Integer> mOpenChatSessionIdList;
    private Integer mPrimaryChatSessionId;

    public ChatManager() {
        mOpenChatSessionIdList = new ArrayList<>();
    }

    /**
     *
     *
     * @param chatSession
     */
    public void focusChatSession(ChatSession chatSession) {

    }

    /**
     * Removes the data that is not needed from memory
     *
     * @param chatSession
     */
    public void loseFocusChatSession(ChatSession chatSession) {

    }

    public List<Integer> getOpenChatSessionIdList() {
        return mOpenChatSessionIdList;
    }

    public Integer getPrimaryChatSessionId() {
        return mPrimaryChatSessionId;
    }

    public void setPrimaryChatSessionId(Integer primaryChatSessionId) {
        mPrimaryChatSessionId = primaryChatSessionId;
    }
}