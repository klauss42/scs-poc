#!/bin/bash
scriptdir="$( cd "$( dirname "$0" )" && pwd )"
if ! which npm >/dev/null 2>&1; then
    echo "npm not found. Please install npm first" 1>&2
    exit 1
fi
if [[ ! -f ${scriptdir}/node_modules/.bin/git2consul ]]; then
    echo "git2consul not found. Installing locally" 1>&2
    (cd ${scriptdir} && npm install git2consul)
fi

${scriptdir}/node_modules/.bin/git2consul --endpoint $1 --port 8500 --config-file $scriptdir/git2consul.json
