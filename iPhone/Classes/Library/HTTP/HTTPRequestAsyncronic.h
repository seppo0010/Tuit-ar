#import <UIKit/UIKit.h>
#import "HTTPRequest.h"

@interface HTTPRequestAsyncronic : HTTPRequest {
	NSURLConnection* connection;
}

+ (int)getPendingRequests;
- (void)cancel;

@end
