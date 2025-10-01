import React from 'react';
import { CSSTransition, SwitchTransition } from 'react-transition-group';
import { Outlet, useLocation } from 'react-router-dom';

export default function AnimatedOutlet() {
  const location = useLocation();
  const key = location.pathname;
  const nodeRef = React.useMemo(() => React.createRef<HTMLDivElement>(), [key]);
  React.useEffect(() => { window.scrollTo({ top: 0, behavior: 'instant' as ScrollBehavior }); }, [key]);

  return (
    <SwitchTransition mode="out-in">
      <CSSTransition
        key={key}
        nodeRef={nodeRef}
        classNames="view"
        timeout={220}
        unmountOnExit
        mountOnEnter
        appear
      >
        <div ref={nodeRef} className="route-view">
          <Outlet />
        </div>
      </CSSTransition>
    </SwitchTransition>
  );
}