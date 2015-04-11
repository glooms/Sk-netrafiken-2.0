//
//  Route.h
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Route : NSObject
@property NSMutableArray *route;
@property NSString *currentStation;

-(id)initWithStation:(NSString*)name andRoute:(NSMutableArray*)route;
-(NSUInteger)getCurrentStationPlace;
-(NSString*)getDestination;
@end
