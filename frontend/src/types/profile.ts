import { IconProps, Icon } from "@tabler/icons-react";

export interface ProfileCardProps {
  title: string;
  icon: React.ForwardRefExoticComponent<IconProps & React.RefAttributes<Icon>>;
  details: ProfileCardPropsDetails[];
}

export interface ProfileCardPropsDetails {
  title: string;
  value: string | number;
}
