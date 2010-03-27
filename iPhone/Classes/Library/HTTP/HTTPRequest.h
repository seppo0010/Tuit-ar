#import <UIKit/UIKit.h>

#define DEFAULT_TIMEOUT 20
#define EDGE_3G_TIMEOUT 60
#define HTTP_DEBUG 1

@interface HTTPRequest : NSObject {
	id callbackObject;
	SEL successSelector;
	SEL failureSelector;
	int timeoutInterval;

	NSMutableDictionary* postFiles;
	NSMutableDictionary* postParameters;
	NSString* postData;
	NSString* url;
	NSString* filename;
	NSString* boundary;

	NSHTTPURLResponse *httpResponse;
	NSMutableData* loadedData;
}

+ (HTTPRequest*) requestURL:(NSURL*)url andCallSelector:(SEL)selector inObject:(id)obj;
+ (HTTPRequest*) requestURL:(NSURL*)url withParams:(NSDictionary*)_params andCallSelector:(SEL)selector inObject:(id)obj;
+ (HTTPRequest*) requestURL:(NSURL*)url useSyncronic:(BOOL)_sync andCallSelector:(SEL)selector inObject:(id)obj;
+ (HTTPRequest*) requestURL:(NSURL*)url useSyncronic:(BOOL)_sync andCallSucessSelector:(SEL)_successSelector andCallFailureSelector:(SEL)_failureSelector inObject:(id)obj;
- (void) requestUrl:(NSURL*)URL;
- (void) setPostData:(NSString*)_postData;
- (void) addParameter:(NSString*)key withValue:(id)value;
- (void) addParameters:(NSDictionary*)parameters;
- (NSData*) getPostData;
+ (NSString*)buildQueryStringFromDictionary:(NSDictionary*)_dictionary;
- (void) retry;
- (int) getDefaultTimeout;
- (void) callSuccess;
- (void) callFailure;
- (NSData*) getAttachedFiles;
- (void) addFileWithParameterName:(NSString*)_parameterName fileName:(NSString*)_fileName fileData:(NSData*)_data andContentType:(NSString*)_contentType;

@property (assign) id callbackObject;
@property SEL successSelector;
@property SEL failureSelector;
@property int timeoutInterval;
@property (readonly) NSData* data;
@property (retain) NSHTTPURLResponse *httpResponse;
@property (retain) NSString *filename;
@property (retain) NSString* boundary;
@property (readonly) NSString* url;

@end
