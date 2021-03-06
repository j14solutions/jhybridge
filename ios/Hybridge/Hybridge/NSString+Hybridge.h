//
//  NSString+Hybridge.h
//  Hybridge
//
//  Copyright (c) 2014 Telefonica I+D. All rights reserved.
//  Licensed under MIT, see LICENSE for more details.
//

#import <Foundation/Foundation.h>

@interface NSString (Hybridge)

+ (instancetype)hyb_javascriptStringWithEvent:(NSString *)event data:(NSDictionary *)data;

+ (instancetype)hyb_JSONStringWithObject:(id)object;

@end
