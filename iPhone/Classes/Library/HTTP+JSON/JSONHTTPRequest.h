//
//  JSONHTTPRequest.h
//  iMom
//
//  Created by Sebastian Waisbrot on 8/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HTTPRequestAsyncronic.h"

@interface JSONHTTPRequest : HTTPRequestAsyncronic {
	id response;
}

@property (retain) id response;

@end
