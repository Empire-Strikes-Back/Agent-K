(ns Agent-K.main
  (:require
   [clojure.core.async :as a
    :refer [chan put! take! close! offer! to-chan! timeout thread
            sliding-buffer dropping-buffer
            go >! <! alt! alts! do-alts
            mult tap untap pub sub unsub mix unmix admix
            pipe pipeline pipeline-async]]
   [clojure.core.async.impl.protocols :refer [closed?]]
   [clojure.java.io]
   [clojure.string]
   [clojure.pprint]
   [clojure.repl]

   [cheshire.core]

   [Agent-K.seed :refer [root op]]
   [Agent-K.window]
   [Agent-K.raisins]
   [Agent-K.coconut]
   [Agent-K.kiwis]
   [Agent-K.salt]
   [Agent-K.microwaved-potatoes]
   [Agent-K.rolled-oats]
   [Agent-K.beans])
  (:import
   (java.io File))
  (:gen-class))

(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defn reload
  []
  (require
   '[Agent-K.seed]
   '[Agent-K.window]
   '[Agent-K.raisins]
   '[Agent-K.coconut]
   '[Agent-K.kiwis]
   '[Agent-K.salt]
   '[Agent-K.microwaved-potatoes]
   '[Agent-K.rolled-oats]
   '[Agent-K.beans]
   :reload))

(defmethod op :ping
  [value]
  (go
    (clojure.pprint/pprint value)
    (put! (:ui-send| root) {:op :pong
                            :from :program
                            :meatbuster :Jesus})))

(defmethod op :pong
  [value]
  (go
    (clojure.pprint/pprint value)))

(defmethod op :game
  [value]
  (go
    ))

(defmethod op :leave
  [value]
  (go
    ))

(defmethod op :discover
  [value]
  (go))

(defmethod op :settings
  [value]
  (go))

(defn ops-process
  [{:keys []
    :as opts}]
  (go
    (loop []
      (when-let [value (<! (:ops| root))]
        (<! (op value))
        (recur)))))

(defn -main
  [& args]
  (println "i am the postmaster of Truro-Massachussets - and i'm ordering you to leave these premises")

  (let []
    (clojure.java.io/make-parents (:program-data-dirpath root))
    (reset! (:stateA root) {})

    (remove-watch (:stateA root) :watch-fn)
    (add-watch (:stateA root) :watch-fn
               (fn [ref wathc-key old-state new-state]

                 (when (not= old-state new-state))))

    (ops-process {})

    (Agent-K.window/process {})))