CREATE TABLE `User` (
                        `userIdx` int PRIMARY KEY AUTO_INCREMENT,
                        `userId` varchar(30) NOT NULL,
                        `password` varchar(100) NOT NULL,
                        `name` varchar(30),
                        `phone` varchar(11),
                        `email` varchar(100),
                        `sex` varchar(7),
                        `profileImg` text,
                        `webSite` varchar(100),
                        `introduction` text,
                        `birth` varchar(20),
                        `nation` varchar(30),
                        `inActive` boolean DEFAULT true,
                        `loginStatus` varchar(10) DEFAULT 'ACTIVE',
                        `status` varchar(10) DEFAULT 'ACTIVE',
                        `createAt` timestamp DEFAULT current_timestamp ,
                        `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Follow` (
                          `followIdx` int PRIMARY KEY AUTO_INCREMENT,
                          `followingIdx` int NOT NULL,
                          `followerIdx` int NOT NULL,
                          `status` varchar(10) DEFAULT 'ACTIVE',
                          `createAt` timestamp DEFAULT current_timestamp,
                          `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Post` (
                        `postIdx` int PRIMARY KEY AUTO_INCREMENT,
                        `userIdx` int NOT NULL,
                        `content` varchar(300) NOT NULL,
                        `status` varchar(10) DEFAULT 'ACTIVE',
                        `createAt` timestamp DEFAULT current_timestamp,
                        `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `PostImg` (
                           `postImgIdx` int PRIMARY KEY AUTO_INCREMENT,
                           `postIdx` int NOT NULL,
                           `postImgUrl` text NOT NULL,
                           `status` varchar(10) DEFAULT 'ACTIVE',
                           `createAt` timestamp DEFAULT current_timestamp,
                           `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `postLike` (
                            `postLikeIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `postIdx` int NOT NULL,
                            `userIdx` int NOT NULL,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `PostReport` (
                            `postReportIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `postIdx` int NOT NULL,
                            `userIdx` int NOT NULL,
                            `reason` varchar(100) ,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `PostsSave` (
                            `postSaveIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `postIdx` int NOT NULL,
                            `userIdx` int NOT NULL,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Comment` (
                           `commentIdx` int PRIMARY KEY AUTO_INCREMENT,
                           `userIdx` int NOT NULL,
                           `postIdx` int NOT NULL,
                           `replyStatus` boolean DEFAULT false,
                           `reply` text NOT NULL,
                           `depth` int NOT NULL,
                           `commentAIdx` int DEFAULT 0,
                           `status` varchar(10) DEFAULT 'ACTIVE',
                           `createAt` timestamp DEFAULT current_timestamp,
                           `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `CommentReport` (
                            `commentReportIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `commentIdx` int NOT NULL,
                            `userIdx` int NOT NULL,
                            `reason` varchar(100) ,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `CommentLike` (
                           `commentLikeIdx` int PRIMARY KEY AUTO_INCREMENT,
                           `userIdx` int NOT NULL,
                           `commentIdx` int NOT NULL,
                           `status` varchar(10) DEFAULT 'ACTIVE',
                           `createAt` timestamp DEFAULT current_timestamp,
                           `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Story` (
                         `storyIdx` int PRIMARY KEY AUTO_INCREMENT,
                         `userIdx` int NOT NULL,
                         `content` varchar(300),
                         `status` varchar(10) DEFAULT 'ACTIVE',
                         `createAt` timestamp DEFAULT current_timestamp,
                         `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `StoryReport` (
                               `StoryReportIdx` int PRIMARY KEY AUTO_INCREMENT,
                               `StoryIdx` int NOT NULL,
                               `userIdx` int NOT NULL,
                               `reason` varchar(100),
                               `status` varchar(10) DEFAULT 'ACTIVE',
                               `createAt` timestamp DEFAULT current_timestamp,
                               `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `StoryViewer` (
                               `StoryViewerIdx` int PRIMARY KEY AUTO_INCREMENT,
                               `StoryIdx` int NOT NULL,
                               `userIdx` int NOT NULL,
                               `status` varchar(10) DEFAULT 'ACTIVE',
                               `createAt` timestamp DEFAULT current_timestamp,
                               `updateAt` timestamp DEFAULT current_timestamp
);


CREATE TABLE `StoryImg` (
                            `storyImgIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `storyImgUrl` text NOT NULL,
                            `storyIdx` int NOT NULL,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `StoryTag` (
                            `storyTagIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `userIdx` int,
                            `storyIdx` int NOT NULL,
                            `locationIdx` int,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `StoryLike` (
                             `storyLikeIdx` int PRIMARY KEY AUTO_INCREMENT,
                             `userIdx` int NOT NULL,
                             `storyIdx` int NOT NULL,
                             `status` varchar(10) DEFAULT 'ACTIVE',
                             `createAt` timestamp DEFAULT current_timestamp,
                             `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Behind` (
                          `behindIdx` int PRIMARY KEY AUTO_INCREMENT,
                          `userIdx` int NOT NULL,
                          `followingIdx` int NOT NULL,
                          `story` int,
                          `post` int,
                          `status` varchar(10) DEFAULT 'ACTIVE',
                          `createAt` timestamp DEFAULT current_timestamp,
                          `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Highlight` (
                             `highlightIdx` int PRIMARY KEY AUTO_INCREMENT,
                             `groupIdx` int NOT NULL,
                             `groupName` varchar(10) NOT NULL,
                             `userIdx` int NOT NULL,
                             `storyIdx` int NOT NULL,
                             'coverImg' text NOT NULL,
                             `status` varchar(10) DEFAULT 'ACTIVE',
                             `createAt` timestamp DEFAULT current_timestamp,
                             `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Search` (
                          `searchIdx` int PRIMARY KEY AUTO_INCREMENT,
                          `userIdx` int NOT NULL,
                          `searchWord` text NOT NULL,
                          `status` varchar(10) DEFAULT 'ACTIVE',
                          `createAt` timestamp DEFAULT current_timestamp,
                          `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `DM` (
                      `dmIdx` int PRIMARY KEY AUTO_INCREMENT,
                      `userIdx` int NOT NULL,
                      `followIdx` int NOT NULL,
                      `status` varchar(10) DEFAULT 'ACTIVE',
                      `createAt` timestamp DEFAULT current_timestamp,
                      `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Service` (
                           `serviceIdx` int PRIMARY KEY AUTO_INCREMENT,
                           `comment` text NOT NULL
);


CREATE TABLE `Audio` (
                         `audioIdx` int PRIMARY KEY AUTO_INCREMENT,
                         `singer` varchar(50) NOT NULL,
                         `sing` text NOT NULL,
                         `audioImgUrl` text NOT NULL,
                         `status` varchar(10) DEFAULT 'ACTIVE',
                         `createAt` timestamp DEFAULT current_timestamp,
                         `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Tag` (
                       `tagIdx` int PRIMARY KEY AUTO_INCREMENT,
                       `content` varchar(50) NOT NULL,
                       `tagImgUrl` text NOT NULL,
                       `status` varchar(10) DEFAULT 'ACTIVE',
                       `createAt` timestamp DEFAULT current_timestamp,
                       `updateAt` timestamp DEFAULT current_timestamp
);

CREATE TABLE `Location` (
                            `locationIdx` int PRIMARY KEY AUTO_INCREMENT,
                            `name` text NOT NULL,
                            `location` text,
                            `locationImgUrl` text NOT NULL,
                            `status` varchar(10) DEFAULT 'ACTIVE',
                            `createAt` timestamp DEFAULT current_timestamp,
                            `updateAt` timestamp DEFAULT current_timestamp
);

