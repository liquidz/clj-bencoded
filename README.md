# bencoded

A Clojure library designed to ... well, that part is up to you.

## Usage

```
function! s:cb(ch, msg) abort
  let data = json_decode(a:msg)
  echom printf('>> %s', data)
endfunction

let g:job = job_start('./bin/bencoded', {'callback': funcref('s:cb')})

let s:ch = job_getchannel(g:job)
call ch_sendraw(s:ch, "d3:fooi123ee\n")

call job_stop(g:job)
```

## License

Copyright © 2018 [Masashi Iizuka](https://twitter.com/uochan)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
