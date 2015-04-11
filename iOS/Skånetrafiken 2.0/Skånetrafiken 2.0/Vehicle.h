//
//  Bus.h
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Route.h"

@interface Vehicle : NSObject
@property NSString *name;
@property NSString *time;
@property Route *stations;
@property bool type;
@property int status;
@property NSString* extra;

-(id)initVehicleWith:(NSString*)name andTime:(NSString*)time andStations:(Route*)station andType:(BOOL)type andStatus:(int)status andExtra:(NSString*)extra;
@end
