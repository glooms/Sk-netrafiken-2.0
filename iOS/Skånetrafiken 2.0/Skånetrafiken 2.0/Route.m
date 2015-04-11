//
//  Route.m
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import "Route.h"

@implementation Route

-(id)initWithStation:(NSString *)name andRoute:(NSMutableArray *)route{
    self = [super init];
    if (self) {
        self.route = route;
        self.currentStation = name;
    }
    return self;
}
-(NSUInteger)getCurrentStationPlace{
    for (int i=0;i<self.route.count;i++) {
        if ([[self.route objectAtIndex:i]isEqualToString:self.currentStation]) {
            return i;
        }
    }
    return self.route.count;
}
-(NSString*)getDestination{
    return [self.route objectAtIndex:self.route.count-1];
}
@end
