/*
 Copyright (C) 2015 Electronic Arts Inc.  All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1.  Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2.  Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3.  Neither the name of Electronic Arts, Inc. ("EA") nor the names of
     its contributors may be used to endorse or promote products derived
     from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY ELECTRONIC ARTS AND ITS CONTRIBUTORS "AS IS" AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL ELECTRONIC ARTS OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ea.orbit.async.test;

import com.ea.orbit.async.Async;
import com.ea.orbit.async.Await;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static com.ea.orbit.async.Await.await;
import static org.junit.Assert.assertEquals;

public class JoinTest
{
    static
    {
        Await.init();
    }

    public static class OtherJoinCalls
    {
        @Async
        public CompletableFuture<Object> doSomething(CompletableFuture<String> blocker)
        {
            int local = 7;
            String res = ":" + Math.max(local, await(blocker).length()) + ":" + join() + ":" + join(2, 3);
            return CompletableFuture.completedFuture(res);
        }

        public Object join()
        {
            return 9;
        }

        public Object join(int a, int b)
        {
            return a + b;
        }
    }

    @Test
    public void testOtherJoinMethods() throws IllegalAccessException, InstantiationException
    {
        OtherJoinCalls a = new OtherJoinCalls();

        CompletableFuture<String> blocker = new CompletableFuture<>();
        final CompletableFuture<Object> res = a.doSomething(blocker);
        blocker.complete("0123456789");
        assertEquals(":10:9:5", res.join());
    }

}
