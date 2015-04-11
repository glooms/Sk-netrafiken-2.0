//
//  Bus.m
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import "Vehicle.h"

@implementation Vehicle
-(id)initVehicleWith:(NSString*)name andTime:(NSString*)time andStations:(Route*)station andType:(BOOL)type andStatus:(int)status andExtra:(NSString *)extra{
    self = [super init];
    if (self) {
        self.name = name;
        self.time = time;
        self.stations = station;
        self.type = type;
        self.status = status;
        self.extra = extra;
    }
    return self;
}

@end
