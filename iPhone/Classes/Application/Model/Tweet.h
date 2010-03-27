//
//  Tweet.h
//  Tuit-ar
//
//  Created by Seppo on 26/03/10.
//  Copyright 2010 Apple Inc. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Tweet : NSObject {
	NSString* tweet_id;
	NSString* username;
	NSString* message;
	NSDate* date;
}

@property (retain) NSString* tweet_id;
@property (retain) NSString* username;
@property (retain) NSString* message;
@property (retain) NSDate* date;

- (Tweet*)initWithDictionary:(NSDictionary*)dictionary;
+ (NSString*) calculateElapsed:(NSDate*)date;

@end
