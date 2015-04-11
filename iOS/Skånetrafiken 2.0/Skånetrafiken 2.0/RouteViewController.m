//
//  RouteViewController.m
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import "RouteViewController.h"
#import "RouteCell.h"

@implementation RouteViewController

@synthesize stations;

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return stations.route.count;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    RouteCell *cell = [tableView dequeueReusableCellWithIdentifier:@"routeCell"];
    cell.nameLabel.text = [stations.route objectAtIndex:indexPath.row];
    
    if (indexPath.row<[stations getCurrentStationPlace]) {
        cell.inBetweenLine.backgroundColor = [UIColor grayColor];
        cell.stationImage.image = [UIImage imageNamed:@"station_sign_gray"];

    }else{
        cell.inBetweenLine.backgroundColor = [UIColor blackColor];
        cell.stationImage.image = [UIImage imageNamed:@"station_sign_black"];
    }
    if (indexPath.row == [stations getCurrentStationPlace]) {
        cell.stationImage.image = [UIImage imageNamed:@"station_sign_red"];
    }else if (indexPath.row == stations.route.count-1){
        [cell.inBetweenLine setHidden:YES];
    }
    
    return cell;
}

- (IBAction)back:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
