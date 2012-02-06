sed -e 's,1,0,' /selinux/enforce > /tmp/enforce && sudo cp /tmp/enforce /selinux/enforce
