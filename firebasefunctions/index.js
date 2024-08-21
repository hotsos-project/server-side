const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


exports.changeLocation = functions.https.onRequest(async (req, res) => {
    const { userLoginId, sido, gugun} = req.body;

    if (!userLoginId || !sido || !gugun) {
        console.error('Invalid request parameters:', { userLoginId, sido, gugun });
        return res.status(400).send('userId and sido and gugun are required.');
    }

    try {
        console.log(`Received request to update favorite region for userId: ${userLoginId}, sido: ${sido}, gugun: ${gugun}`);

        const userRef = admin.firestore().collection('users').doc(userLoginId);

        await userRef.update({
            sido: sido,
            gugun: gugun
        });
        console.log(`region updated for userId: ${userLoginId}`);
        return res.status(200).send('Favorite region updated successfully');
    } catch (error) {
        console.error('Error updating favorite region:', error);
        return res.status(500).send('Error updating favorite region: ' + error.message);
    }

});

exports.getDisasterMessagesOfMeAndFollowersPagination = functions.https.onRequest(async (req, res) => {
    const { userLoginId, seconds, limitPage } = req.body;

    if (!userLoginId || !limitPage) {
        console.error('Invalid request parameters:', { userLoginId, limitPage });
        return res.status(400).send('userLoginId and limitPage are required.');
    }

    try {
        console.log(`Received request to get personal disaster messages for userLoginId: ${userLoginId}`);

        // 사용자 및 양방향 팔로워 찾기
        const followersSnapshot = await admin.firestore().collection('followers').get();

        const followingList = followersSnapshot.docs
            .filter(doc => Object.keys(doc.data())[0] === userLoginId)
            .map(doc => Object.values(doc.data())[0]);

        const followersList = followersSnapshot.docs
            .filter(doc => Object.values(doc.data())[0] === userLoginId)
            .map(doc => Object.keys(doc.data())[0]);

        const mutualFollowers = followingList.filter(followingId => followersList.includes(followingId));

        // 사용자와 팔로워의 personal 알람 중 disaster 알람 수집
        const allUserIds = [userLoginId, ...mutualFollowers];
        let allAlarms = {};

        for (const userId of allUserIds) {
            const alarmsSnapshot = await admin.firestore().collection('users').doc(userId).collection('alarms')
                .where('notificationType', '==', 'personal')
                .where('alarmType', '==', 'disaster')
                .get();

            alarmsSnapshot.forEach(doc => {
                const alarmData = doc.data();
                const keyId = alarmData.keyId;  // alarmData에서 keyId 필드를 가져옴
                const docId = doc.id;

                if (!allAlarms[keyId]) {
                    allAlarms[keyId] = {
                        keyId: keyId,
                        isMine: userId === userLoginId,
                        isRead: userId === userLoginId ? alarmData.isRead || 'Y' : 'Y',
                        followers: [],
                        id: docId,
                        timestamp: alarmData.timestamp
                    };
                }

                if (userId !== userLoginId) {
                    allAlarms[keyId].followers.push(userId);
                }
            });
        }

        // Object를 배열로 변환하여 timestamp 기준으로 정렬
        let responseAlarms = Object.values(allAlarms).map(alarm => ({
            keyId: alarm.keyId,
            isMine: alarm.isMine,
            isRead: alarm.isMine ? alarm.isRead : 'Y',
            id: alarm.isMine ? alarm.id : '',
            followerLoginId: alarm.followers.length === 0 ? "" : alarm.followers[0],
            followersCount: alarm.followers.length,
            timestamp: alarm.timestamp
        }));

        // timestamp 기준으로 최신순 정렬
        responseAlarms.sort((a, b) => b.timestamp - a.timestamp);

        // 페이지네이션 처리
        if (seconds) {
            responseAlarms = responseAlarms.filter(alarm => alarm.timestamp._seconds < seconds);
        }

        // limitPage 만큼 자르기
        responseAlarms = responseAlarms.slice(0, limitPage);

        return res.status(200).send(responseAlarms);
    } catch (error) {
        console.error('Error fetching personal disaster messages:', error);
        return res.status(500).send('Error fetching personal disaster messages: ' + error.message);
    }
});


exports.getDisasterMessagesOfMeAndFollowers = functions.https.onRequest(async (req, res) => {
    const { userLoginId } = req.body;

    if (!userLoginId) {
        console.error('Invalid request parameters:', { userLoginId });
        return res.status(400).send('userLoginId is required.');
    }

    try {
        console.log(`Received request to get personal disaster messages for userLoginId: ${userLoginId}`);

        // 사용자 및 양방향 팔로워 찾기
        const followersSnapshot = await admin.firestore().collection('followers').get();

        const followingList = followersSnapshot.docs
            .filter(doc => Object.keys(doc.data())[0] === userLoginId)
            .map(doc => Object.values(doc.data())[0]);

        const followersList = followersSnapshot.docs
            .filter(doc => Object.values(doc.data())[0] === userLoginId)
            .map(doc => Object.keys(doc.data())[0]);

        const mutualFollowers = followingList.filter(followingId => followersList.includes(followingId));

        // 사용자와 팔로워의 personal 알람 중 disaster 알람 수집
        const allUserIds = [userLoginId, ...mutualFollowers];
        let allAlarms = {};

        for (const userId of allUserIds) {
            const alarmsSnapshot = await admin.firestore().collection('users').doc(userId).collection('alarms')
                .where('notificationType', '==', 'personal')
                .where('alarmType', '==', 'disaster')
                .get();

            alarmsSnapshot.forEach(doc => {
                const alarmData = doc.data();
                const keyId = alarmData.keyId;  // alarmData에서 keyId 필드를 가져옴
                const docId = doc.id;

                if (!allAlarms[keyId]) {
                    allAlarms[keyId] = {
                        keyId: keyId,
                        isMine: userId === userLoginId,
                        isRead: userId === userLoginId ? alarmData.isRead || 'Y' : 'Y',
                        followers: [],
                        id : docId
                    };
                }

                if (userId !== userLoginId) {
                    allAlarms[keyId].followers.push(userId);
                }
            });
        }

        // Object를 배열로 변환하여 응답 준비
        const responseAlarms = Object.values(allAlarms).map(alarm => ({
            keyId: alarm.keyId,
            isMine: alarm.isMine,
            isRead: alarm.isMine ? alarm.isRead : 'Y',
            id: alarm.isMine ? alarm.id : '',
            followerLoginId: alarm.followers.length === 0 ? "" : alarm.followers[0],
            followersCount: alarm.followers.length
        }));

        return res.status(200).send(responseAlarms);
    } catch (error) {
        console.error('Error fetching personal disaster messages:', error);
        return res.status(500).send('Error fetching personal disaster messages: ' + error.message);
    }
});


exports.getAlarmsPagination = functions.https.onRequest(async (req, res) => {
    const { userLoginId, seconds, limitPage } = req.body;

    if (!userLoginId || !limitPage) {
        console.error('Invalid request parameters:', { userLoginId, limitPage });
        return res.status(400).send('userLoginId and limitPage are required.');
    }


    try {
        console.log(`Received request to get alarms for userLoginId: ${userLoginId}`);

        const userRef = admin.firestore().collection('users').doc(userLoginId);
        const alarmsRef = userRef.collection('alarms');
        
        const snapshot = await alarmsRef
            .where('alarmType', 'in', ['comment', 'hot_issue'])
            .get();

        if (snapshot.empty) {
            console.log(`No alarms found for userLoginId: ${userLoginId} with specified alarmType.`);
            return res.status(404).send('No matching alarms found.');
        }

        let alarms = [];
        snapshot.forEach(doc => {
            let alarmData = doc.data();
            let sendTime;
            // Convert Firestore timestamp to desired format
            if (alarmData.timestamp && alarmData.timestamp.toDate) {
                const date = alarmData.timestamp.toDate();
                const formattedTimestamp = date.toISOString().replace('T', ' ').substring(0, 19);
                sendTime = formattedTimestamp;
            }

            alarms.push({ id: doc.id, ...alarmData, sendTime });
        });
        
        // timestamp 기준으로 최신순 정렬
        alarms.sort((a, b) => b.timestamp - a.timestamp);

        // 페이지네이션 처리
        if (seconds) {
            alarms = alarms.filter(alarm => alarm.timestamp._seconds < seconds);
        }

        // limitPage 만큼 자르기
        alarms = alarms.slice(0, limitPage);

        return res.status(200).json(alarms);
    } catch (error) {
        console.error('Error fetching alarms:', error);
        return res.status(500).send('Error fetching alarms: ' + error.message);
    }
});



exports.getAlarms = functions.https.onRequest(async (req, res) => {
    const { userLoginId } = req.body;

    if (!userLoginId) {
        console.error('Invalid request parameters:', { userLoginId });
        return res.status(400).send('userLoginId is required.');
    }

    try {
        console.log(`Received request to get alarms for userLoginId: ${userLoginId}`);

        const userRef = admin.firestore().collection('users').doc(userLoginId);
        const alarmsRef = userRef.collection('alarms');
        
        const snapshot = await alarmsRef
            .where('alarmType', 'in', ['comment', 'hot_issue'])
            .get();

        if (snapshot.empty) {
            console.log(`No alarms found for userLoginId: ${userLoginId} with specified alarmType.`);
            return res.status(404).send('No matching alarms found.');
        }

        let alarms = [];
        snapshot.forEach(doc => {
            let alarmData = doc.data();
            let sendTime;
            // Convert Firestore timestamp to desired format
            if (alarmData.timestamp && alarmData.timestamp.toDate) {
                const date = alarmData.timestamp.toDate();
                const formattedTimestamp = date.toISOString().replace('T', ' ').substring(0, 19);
                sendTime = formattedTimestamp;
            }

            alarms.push({ id: doc.id, ...alarmData, sendTime });
        });

        return res.status(200).json(alarms);
    } catch (error) {
        console.error('Error fetching alarms:', error);
        return res.status(500).send('Error fetching alarms: ' + error.message);
    }
});


exports.readAlarm = functions.https.onRequest(async (req, res) => {
    const data = req.body;
    const { userLoginId, userAlarmId } = data;

    if (!userLoginId || !userAlarmId) {
        console.error('Invalid request parameters:', { userLoginId, userAlarmId });
        return res.status(400).send('userId and alarmId are required.');
    }

    try {
        console.log(`Received request to mark alarm as read for userId: ${userLoginId}, alarmId: ${userAlarmId}`);

        // Firestore에서 특정 사용자의 특정 알람을 조회
        const alarmRef = admin.firestore().collection('users').doc(userLoginId).collection('alarms').doc(userAlarmId);
        const alarmDoc = await alarmRef.get();

        if (!alarmDoc.exists) {
            console.log(`No matching alarm found for alarmId: ${userAlarmId}`);
            return res.status(404).send('No matching alarm found.');
        }

        // 알람의 isRead 필드를 'Y'로 업데이트
        await alarmRef.update({
            isRead: 'Y'
        });
        return res.status(200).send("OK");
    } catch (error) {
        console.error('Error marking alarm as read:', error);
        return res.status(500).send('Error marking alarm as read: ' + error.message);
    }
});


exports.updateFavoriteRegion = functions.https.onRequest(async (req, res) => {
    const data = req.body;
    const { userLoginId, favoriteSido, favoriteGugun, isAdd } = data;

    if (!userLoginId || !isAdd) {
        console.error('Invalid request parameters:', { userLoginId, isAdd });
        return res.status(400).send('userId and isAdd are required.');
    }

    try {
        console.log(`Received request to update favorite region for userId: ${userLoginId}, isAdd: ${isAdd}`);

        const userRef = admin.firestore().collection('users').doc(userLoginId);

        if (isAdd === 'Y') {
            // favoriteSido, favoriteGugun 값을 추가
            if (typeof favoriteSido === 'undefined' || typeof favoriteGugun === 'undefined') {
                return res.status(400).send('favoriteSido and favoriteGugun are required when isAdd is "Y".');
            }

            await userRef.update({
                favoriteSido: favoriteSido,
                favoriteGugun: favoriteGugun
            });
            console.log(`Favorite region updated for userId: ${userLoginId}`);
        } else if (isAdd === 'N') {
            // favoriteSido, favoriteGugun 값을 빈 값으로 업데이트
            await userRef.update({
                favoriteSido: '',
                favoriteGugun: ''
            });
            console.log(`Favorite region cleared for userId: ${userLoginId}`);
        } else {
            console.error('Invalid isAdd value:', isAdd);
            return res.status(400).send('Invalid value for isAdd. It must be either "Y" or "N".');
        }

        return res.status(200).send('Favorite region updated successfully');
    } catch (error) {
        console.error('Error updating favorite region:', error);
        return res.status(500).send('Error updating favorite region: ' + error.message);
    }
});


exports.join = functions.https.onRequest(async (req, res) => {
    const { userId, fcmToken, sido, gugun } = req.body;

    if (!userId || !fcmToken || !sido || !gugun) {
        console.error('Invalid request parameters:', { userId, fcmToken, sido, gugun });
        return res.status(400).send('userId, fcmToken, sido, and gugun are required.');
    }

    try {
        await admin.firestore()
            .collection('users')
            .doc(userId)
            .set({
                token: fcmToken,
                sido: sido,
                gugun: gugun,
                favoriteSido: "",
                favoriteGugun: "",
            });
        console.log('User data saved to Firestore:', { userId, sido, gugun });
        return res.status(200).send('User data saved successfully');
    } catch (error) {
        console.error('Error saving user data to Firestore:', error);
        return res.status(500).send('Error saving user data');
    }
});

exports.handleFollow = functions.https.onRequest(async (req, res) => {
    const { memberLoginId, followLoginId, isFollow } = req.body;

    if (!memberLoginId || !followLoginId || !isFollow) {
        console.error('Invalid request parameters:', { memberLoginId, followLoginId, isFollow });
        return res.status(400).send('memberLoginId, followLoginId, and isFollow are required.');
    }

    try {
        const followersRef = admin.firestore().collection('followers');

        if (isFollow === 'N') {
            // 팔로우 추가: memberLoginId를 key로, followLoginId를 value로 하는 새로운 문서를 추가
            const newFollowData = {};
            newFollowData[memberLoginId] = followLoginId;

            await followersRef.add(newFollowData);
            console.log(`Successfully followed: ${memberLoginId} -> ${followLoginId}`);
            return res.status(200).send(`Successfully followed: ${memberLoginId} -> ${followLoginId}`);
        } else if (isFollow === 'Y') {
            // 팔로우 취소: memberLoginId를 key로, followLoginId를 value로 하는 문서를 삭제
            const snapshot = await followersRef.where(memberLoginId, '==', followLoginId).get();
            if (!snapshot.empty) {
                const batch = admin.firestore().batch();
                snapshot.forEach(doc => {
                    batch.delete(doc.ref);
                });
                await batch.commit();
                console.log(`Successfully unfollowed: ${memberLoginId} -> ${followLoginId}`);
                return res.status(200).send(`Successfully unfollowed: ${memberLoginId} -> ${followLoginId}`);
            } else {
                console.log('No matching document found to unfollow.');
                return res.status(404).send('No matching document found to unfollow.');
            }
        } else {
            console.error('Invalid isFollow value:', isFollow);
            return res.status(400).send('Invalid isFollow value.');
        }
    } catch (error) {
        console.error('Error processing follow/unfollow:', error);
        return res.status(500).send('Internal Server Error.');
    }
});



exports.sendRegionNotificationWithFollows = functions.https.onRequest(async (req, res) => {
    const data = req.body;
    const { alarmType, keyword, textList, title, content, sido, gugun, keyId, alarmId } = data;

    if (!sido || !title || !content || !alarmType || !keyword || !textList) {
        console.error('Invalid request parameters:', { sido, gugun, title, content, alarmType, keyword, textList });
        return res.status(400).send('sido, title, content, alarmType, keyword, and textList are required.');
    }

    try {
        console.log(`Received request to send notification to sido: ${sido}, gugun: ${gugun || 'all'}, title: ${title}, content: ${content}`);

        const usersRef = admin.firestore().collection('users');
        let snapshot;

        // 1. sido와 gugun이 일치하는 사용자 검색
        if (gugun) {
            snapshot = await usersRef.where('sido', '==', sido).where('gugun', '==', gugun).get();
        } else {
            snapshot = await usersRef.where('sido', '==', sido).get();
        }

        console.log(`Number of matching users: ${snapshot.size}`);
        if (snapshot.empty) {
            console.warn('No matching documents.');
            return res.status(404).send('No users found in the specified region.');
        }

        const followersSnapshot = await admin.firestore().collection('followers').get();

        let regionTokens = new Set();
        let followTokens = new Set();
        let interestTokens = new Set();
        let batch = admin.firestore().batch();

        for (const doc of snapshot.docs) {
            const userData = doc.data();
            const userId = doc.id;
            if (userData.token) {
                regionTokens.add(userData.token);
            }

            const alarmData = {
                alarmType: alarmType,
                keyword: keyword,
                textList: textList,
                title: title,
                content: content,
                timestamp: admin.firestore.FieldValue.serverTimestamp(),
                isRead : 'N',
                keyId : keyId,
                alarmId : alarmId,
                notificationType : 'personal'
            };

            const userAlarmsRef = doc.ref.collection('alarms').doc();
            batch.set(userAlarmsRef, alarmData);

            // 2. 양방향 팔로우 관계 사용자들 검색 및 알림 추가
            const followingList = followersSnapshot.docs
                .filter(followerDoc => Object.keys(followerDoc.data())[0] === userId)
                .map(followerDoc => Object.values(followerDoc.data())[0]);

            const followersList = followersSnapshot.docs
                .filter(followerDoc => Object.values(followerDoc.data())[0] === userId)
                .map(followerDoc => Object.keys(followerDoc.data())[0]);

            const mutualList = followingList.filter(followingId => followersList.includes(followingId));

            for (const friendId of mutualList) {
                const friendRef = admin.firestore().collection('users').doc(friendId);
                const friendDoc = await friendRef.get();

                if (friendDoc.exists && friendDoc.data().token) {
                    followTokens.add(friendDoc.data().token);

                    const followAlarmData = {
                        alarmType: alarmType,
                        keyword: keyword,
                        textList: textList,
                        title: title,
                        content: content,
                        timestamp: admin.firestore.FieldValue.serverTimestamp(),
                        isRead : 'N',
                        keyId : keyId,
                        alarmId : alarmId,
                        notificationType : 'follow'
                    };

                    const friendAlarmsRef = friendRef.collection('alarms').doc();
                    batch.set(friendAlarmsRef, followAlarmData);
                }
            }
        }

        // 3. 관심지역에 해당하는 사용자들에게 알림 전송 (시도와 구군이 일치하는 사람들 제외)
        const interestSnapshot = await usersRef
            .where('favoriteSido', '==', sido)
            .where('favoriteGugun', '==', gugun || null) // gugun이 null일 경우 전체 시도 내 관심지역으로 가정
            .get();

        for (const doc of interestSnapshot.docs) {
            const userData = doc.data();
            const userId = doc.id;

            if (!regionTokens.has(userData.token) && userData.token) { // 이미 지역 알림을 받은 사람 제외
                interestTokens.add(userData.token);

                const interestAlarmData = {
                    alarmType: alarmType,
                    keyword: keyword,
                    textList: textList,
                    title: title,
                    content: content,
                    timestamp: admin.firestore.FieldValue.serverTimestamp(),
                    isRead : 'N',
                    keyId : keyId,
                    alarmId : alarmId,
                    notificationType : 'favoriteRegion'
                };

                const interestAlarmsRef = doc.ref.collection('alarms').doc();
                batch.set(interestAlarmsRef, interestAlarmData);
            }
        }

        try {
            await batch.commit();
            console.log(`Successfully saved alarms to Firestore for users in region: ${sido}-${gugun || 'all'}`);
        } catch (batchError) {
            console.error('Error committing batch:', batchError);
            return res.status(500).send('Error saving alarms to Firestore.');
        }

        const regionTokensArray = Array.from(regionTokens);
        const followTokensArray = Array.from(followTokens);
        const interestTokensArray = Array.from(interestTokens);

        if (regionTokensArray.length > 0) {
            const regionPayload = {
                notification: {
                    title: title,
                    body: content,
                },
                tokens: regionTokensArray,
            };

            try {
                const regionResponse = await admin.messaging().sendMulticast(regionPayload);
                console.log('Successfully sent region message:', regionResponse);

                if (regionResponse.failureCount > 0) {
                    const failedRegionTokens = [];
                    regionResponse.responses.forEach((resp, idx) => {
                        if (!resp.success) {
                            failedRegionTokens.push(regionTokensArray[idx]);
                        }
                    });
                    console.warn('List of region tokens that caused failures: ' + failedRegionTokens);
                }
            } catch (regionError) {
                console.error('Error sending region messages:', regionError);
            }
        } else {
            console.log('No FCM tokens found for the specified region.');
        }

        if (followTokensArray.length > 0) {
            const followPayload = {
                notification: {
                    title: `${title} (팔로우 알림)`,
                    body: `알림: ${content} (팔로우 알림)`,
                },
                tokens: followTokensArray,
            };

            try {
                const followResponse = await admin.messaging().sendMulticast(followPayload);
                console.log('Successfully sent follow message:', followResponse);

                if (followResponse.failureCount > 0) {
                    const failedFollowTokens = [];
                    followResponse.responses.forEach((resp, idx) => {
                        if (!resp.success) {
                            failedFollowTokens.push(followTokensArray[idx]);
                        }
                    });
                    console.warn('List of follow tokens that caused failures: ' + failedFollowTokens);
                }
            } catch (followError) {
                console.error('Error sending follow messages:', followError);
            }
        } else {
            console.log('No FCM tokens found for followers.');
        }

        if (interestTokensArray.length > 0) {
            const interestPayload = {
                notification: {
                    title: `${title} (관심지역 알림)`,
                    body: `알림: ${content} (관심지역 알림)`,
                },
                tokens: interestTokensArray,
            };

            try {
                const interestResponse = await admin.messaging().sendMulticast(interestPayload);
                console.log('Successfully sent interest message:', interestResponse);

                if (interestResponse.failureCount > 0) {
                    const failedInterestTokens = [];
                    interestResponse.responses.forEach((resp, idx) => {
                        if (!resp.success) {
                            failedInterestTokens.push(interestTokensArray[idx]);
                        }
                    });
                    console.warn('List of interest tokens that caused failures: ' + failedInterestTokens);
                }
            } catch (interestError) {
                console.error('Error sending interest messages:', interestError);
            }
        } else {
            console.log('No FCM tokens found for interest region.');
        }

        return res.status(200).send('Messages sent successfully');
    } catch (error) {
        console.error('Error sending message:', error);
        return res.status(500).send('Error sending messages: ' + error.message);
    }
});



exports.sendBoardOwner = functions.https.onRequest(async (req, res) => {
    const data = req.body;
    const { alarmType, keyword, textList, title, content, userId, keyId, alarmId  } = data;
    

    // 필수 파라미터 확인
    if (!userId || !title || !content) {
        console.error('Invalid request parameters:', { userId, title, content });
        return res.status(400).send('userId, title, and content are required.');
    }

    try {
        console.log(`Received request to send notification to userId: ${userId}`);

        // Firestore에서 userId가 일치하는 사용자 조회
        const userRef = admin.firestore().collection('users').doc(userId);
        const userDoc = await userRef.get();

        if (!userDoc.exists) {
            console.log(`No matching user found for userId: ${userId}`);
            return res.status(404).send('No matching user found.');
        }

        const userData = userDoc.data();

        if (!userData.token) {
            console.log(`No FCM token found for userId: ${userId}`);
            return res.status(404).send('No FCM token found for the specified user.');
        }

        // 알람 데이터를 사용자 문서의 alarms 하위 컬렉션에 저장
        const alarmData = {
            alarmType: alarmType || 'default',  // 기본값 설정
            keyword: keyword || '',
            textList: textList || [],
            title: title,
            content: content,
            timestamp: admin.firestore.FieldValue.serverTimestamp(),
            keyId : keyId,
            alarmId : alarmId,
            isRead : 'N',
            notificationType : 'personal'
        };

        try {
            const userAlarmsRef = userRef.collection('alarms').doc();
            await userAlarmsRef.set(alarmData);
            console.log(`Alarm data saved for userId: ${userId}`);
        } catch (error) {
            console.error('Error saving alarm data:', error);
            return res.status(500).send('Error saving alarm data: ' + error.message);
        }

        // 알람 데이터가 성공적으로 저장된 후 FCM 알림 전송
        const token = userData.token;

        const payload = {
            notification: {
                title: title,
                body: content,
            },
            token: token,
        };

        const response = await admin.messaging().send(payload);
        console.log('Successfully sent message:', response);

        return res.status(200).send('Notification sent and alarm data saved successfully');
    } catch (error) {
        console.error('Error sending message:', error);
        return res.status(500).send('Error sending message: ' + error.message);
    }
});
