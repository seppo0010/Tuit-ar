//
//  JSONHTTPRequest.m
//  iMom
//
//  Created by Sebastian Waisbrot on 8/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "JSONHTTPRequest.h"
#import "NSString+SBJSON.h"

@implementation JSONHTTPRequest

@synthesize response;

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
	NSString* s = [[[NSString alloc] initWithData:loadedData encoding:NSUTF8StringEncoding] autorelease];
	self.response = [s JSONValue];
	[self callSuccess];
}

- (void) dealloc {
	self.response = nil;
	[super dealloc];
}

@end
