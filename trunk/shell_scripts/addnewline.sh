for f in /tmp/compare/cakepfelance2/app/controllers/*.php;do sed '${/^$/!s/$/\
/;}' "$f">"$f"_ && mv "$f"_ "$f"
done
