import { IconProps, Icon } from "@tabler/icons-react";

export interface ISidebarButton {
  name: string;
  path: string;
  param?: string;
  icon: React.ForwardRefExoticComponent<IconProps & React.RefAttributes<Icon>>;
  isAdminOnly: boolean;
}
