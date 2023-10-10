//Name: Faraz Ahmad
//FSUID: fa21i@fsu.edu
#include<stdio.h>
#include<stdlib.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<unistd.h>
#include<string.h>
#include <arpa/inet.h>

int main(int argc, char* argv[]){
    int error_check;
    
    //creating connection socket for client
    int connection_socket;
    connection_socket=socket(AF_INET,SOCK_STREAM,0);
    
    // specify an address for the socket to connect to
    struct sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_port =(short)atoi(argv[2]);
    server_address.sin_addr.s_addr =INADDR_ANY;

    int connection_status=connect(connection_socket,(struct sockaddr*)&server_address,sizeof(server_address));
    if (connection_status == -1)
		printf("error in establishing connection with the server\n");
    
    char connection_msg[256];
    fd_set descp_set;
    while(1){
        FD_ZERO(&descp_set);
		FD_SET(0,&descp_set);
		FD_SET(connection_socket,&descp_set);
        error_check=select(5,&descp_set,0,0,0);
        if(error_check==-1){
            printf("error in select operation");
            close(connection_socket);
            return 0;
        }
        else if(FD_ISSET(0,&descp_set)){
            
            fgets(connection_msg,sizeof(connection_msg),stdin);
            error_check=send(connection_socket,&connection_msg,sizeof(connection_msg),0);
            
            if(error_check==-1){
                perror("error msg: error in sending message to server\n\n");
                return 0;
            }
            if(strncmp(connection_msg,"bye",3)==0){
            
                close(connection_socket);
                return 0;
            }
        }
        else if(FD_ISSET(connection_socket,&descp_set)){
            error_check=recv(connection_socket,&connection_msg,sizeof(connection_msg),0);
            if(error_check==-1){
                perror("error msg:msg wasnt recieved from server\n");
                return 0;
            }
            if(strncmp(connection_msg,"bye",3)==0){
            
                close(connection_socket);
                return 0;
            }
            printf("ser: %s\n", connection_msg);
        }
    }
    close(connection_socket);
    return 0;
    }