package com.sparos.uniquone.msachatservice.utils;

public class ChatUtils {
    /*public static ChatRoomDto entityToDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .ch(chatRoom.getId())
                .userId(chatRoom.getUserId())
                .otherUserId(chatRoom.getOtherUserId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .build();
    }*/


    /*public static ChatRoomDto entityToChatRoomDto(ChatRoom chatRoom, Long userId) {
        ChatRoomType chatRoomType = chatRoom.getType();
        Long otherUserId = chatRoom.getOtherUserId();

        if (chatRoom.getOtherUserId().equals(userId) && chatRoom.getType().equals(ChatRoomType.SELLER)){
            chatRoomType = ChatRoomType.BUYER;
            otherUserId = chatRoom.getUserId();
        }else if(chatRoom.getOtherUserId().equals(userId) && chatRoom.getType().equals(ChatRoomType.BUYER)){
            chatRoomType = ChatRoomType.SELLER;
            otherUserId = chatRoom.getUserId();
        }

        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .otherUserId(otherUserId)
                .name(chatRoom.getName())
                .type(chatRoomType)
                .build();
    }


    public static ChatOutputDto entityToChatOutputDto(Chat chat) {
        return ChatOutputDto.builder()
                .sender(chat.getSender())
                .message(chat.getMsg())
                .build();
    }

    public static ChatRoom dtoToEntity(ChatRoomDto chatRoomDto) {
        return ChatRoom.builder()
                .name(chatRoomDto.getName())
                .otherUserId(chatRoomDto.getOtherUserId())
                .build();
    }*/

    /*public static ChatRoomDto test(ChatRoomDto chatRoom, UserDto otherUserDto, Long userId) {
        ChatRoomType chatRoomType = chatRoom.getType();
        Long otherUserId = chatRoom.getOtherUserId();
        System.err.println("내 id : " + userId);
        System.err.println("너 닉네임 : " + otherUserDto.getName());
        if (chatRoom.getOtherUserId().equals(userId) && chatRoom.getType().equals(ChatRoomType.SELLER)){
            chatRoomType = ChatRoomType.BUYER;
            otherUserId = otherUserDto.getUserId();
        }else if(chatRoom.getOtherUserId().equals(userId) && chatRoom.getType().equals(ChatRoomType.BUYER)){
            chatRoomType = ChatRoomType.SELLER;
            otherUserId = otherUserDto.getUserId();
        }

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .otherUserId(otherUserDto.getUserId())
                .otherUserName(otherUserDto.getName())
                .type(chatRoom.getType())
                .build();
    }

    public static ChatRoomDto dtoToDto(UserDto userDto, UserDto otherUserDto, ChatRoomDto chatRoomDto, Long userId) {

        ChatRoomType chatRoomType = chatRoomDto.getType();
        Long otherUserId = otherUserDto.getUserId();
        String otherUserName = otherUserDto.getName();

        if (chatRoomDto.getOtherUserId().equals(userId) && chatRoomType.equals(ChatRoomType.SELLER)){
            chatRoomType = ChatRoomType.BUYER;
            otherUserId = userDto.getUserId();
            otherUserName = userDto.getName();
        }else if(chatRoomDto.getOtherUserId().equals(userId) && chatRoomType.equals(ChatRoomType.BUYER)){
            chatRoomType = ChatRoomType.SELLER;
            otherUserId = userDto.getUserId();
            otherUserName = userDto.getName();
        }

        return ChatRoomDto.builder()
                .roomId(chatRoomDto.getRoomId())
                .name(chatRoomDto.getName())
                .otherUserId(otherUserId)
                .otherUserName(otherUserName)
                .type(chatRoomType)
                .build();
    }

    public static ChatRoomDto dtoToDto2(UserDto otherUserDto, ChatRoomDto chatRoomDto) {
        System.err.println("djldjld : " + chatRoomDto.getName());
        return ChatRoomDto.builder()
                .roomId(chatRoomDto.getRoomId())
                .name(chatRoomDto.getName())
                .otherUserId(otherUserDto.getUserId())
                .otherUserName(otherUserDto.getName())
                .type(chatRoomDto.getType())
                .build();
    }




    public static ChatRoom roomInputDtoToEntity(ChatRoomInputDto chatRoomInputDto) {
        return ChatRoom.builder()
                .name(chatRoomInputDto.getName())
                .userId(chatRoomInputDto.getUserId())
                .otherUserId(chatRoomInputDto.getOtherUserId())
                .type(chatRoomInputDto.getType())
                .build();
    }*/
}
