//
//  RouteCell.h
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RouteCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *stationImage;
@property (weak, nonatomic) IBOutlet UIView *inBetweenLine;

@end
